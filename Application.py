"""
Created on: 30 April 2025
Environment Preparation: pip install pandas numpy matplotlib seaborn scikit-learn imbalanced-learn xgboost shap
@author: IOM209
"""



# ================== Dependency Libray Import ==================
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from imblearn.over_sampling import SMOTE
from sklearn.linear_model import LogisticRegression
from xgboost import XGBClassifier
from sklearn.ensemble import StackingClassifier
from sklearn.metrics import classification_report, roc_curve, auc, confusion_matrix
from sklearn.metrics import precision_recall_fscore_support
import shap



# ====================== Step1: Label Definition ======================
print("\n\033[1;34m=== 1: Label Definition ===\033[0m")

# ------ Read Data from credit_record.csv ------
credit = pd.read_csv("credit_record.csv")
print("\n\033[1;32m[Example of Original Credit Record]:\033[0m")
print(credit.head(11))


# ------ Label Definition Rules ------
def define_label(status_series):
    if any(status in ['3','4','5'] for status in status_series):
        return 1  # "Bad" Clients
    elif all(status in ['C','X'] for status in status_series):
        return 0  # "Good" Clients
    else:
        return 0  # Rest of the cases are conservatively classified as "Good" Clients

# ------ Generate Label Data ------
labels = credit.groupby('ID')['STATUS'].apply(define_label).reset_index(name='TARGET')

# ------ Output Label Distribution ------
print("\n\033[1;32m[Label Distribution Statistics]:\033[0m")
print(labels['TARGET'].value_counts())

# ------ Visualization of Label Distribution ------
plt.figure(figsize=(6,4))
sns.countplot(x='TARGET', data=labels, hue='TARGET', palette=['green', 'red'], legend=False)
plt.title("Label Distribution (0=Good, 1=Bad)")
plt.xticks([0, 1], ['Good Clients', 'Bad Clients'])
plt.show()



# ================== Step2: Data Cleaning and Feature Engineering ==================
print("\n\033[1;34m=== 2. Data Cleaning and Feature Engineering ===\033[0m")

# ------ Read Data from application_record.csv ------
app = pd.read_csv("application_record.csv")
print("\n\033[1;32m[Example of Original Application Record]:\033[0m")
print(app.head(11))

# ------ Data Merge ------
merged_data = pd.merge(app, labels, on='ID', how='inner')

# ------ Handle with Outliers ------
def handle_outliers(df):
    df['DAYS_EMPLOYED'] = df['DAYS_EMPLOYED'].apply(lambda x: 0 if x > 0 else x)  # Positive number indicates unemployment and is set to zero
    df['CNT_CHILDREN'] = df['CNT_CHILDREN'].apply(lambda x: x if x >= 0 else 0)  # The number of children is not negative
    df['AMT_INCOME_TOTAL'] = df['AMT_INCOME_TOTAL'].apply(lambda x: x if x > 0 else np.nan)  # Income is greater than zero
    df['CNT_FAM_MEMBERS'] = df['CNT_FAM_MEMBERS'].apply(lambda x: x if x >= 1 else 1)  # The number of family members is at least one
    return df
merged_data = handle_outliers(merged_data)

# ------ Handle with Missing Values ------
def handle_missing_values(df):
    cat_cols = ['OCCUPATION_TYPE', 'NAME_INCOME_TYPE', 'NAME_EDUCATION_TYPE',
                'NAME_FAMILY_STATUS', 'NAME_HOUSING_TYPE']
    df.fillna({col: 'Unknown' for col in cat_cols}, inplace=True)  # The variables for classification are filled as "Unknown"
    num_cols = ['AMT_INCOME_TOTAL', 'CNT_CHILDREN', 'CNT_FAM_MEMBERS']  # The numerical variable is filled with Medium
    for col in num_cols:
        median_val = df[col].median()
        df[col] = df[col].fillna(median_val)
    return df
merged_data = handle_missing_values(merged_data)

# ------ Result of Data Cleaning ------
def print_processing_summary(df):

    # -------- Missing Value Statistics --------
    print("\n\033[1;32m[Result of Data Cleaning (Missing Value Statistics)]:\033[0m")
    cat_cols = ['OCCUPATION_TYPE', 'NAME_INCOME_TYPE', 'NAME_EDUCATION_TYPE',
                'NAME_FAMILY_STATUS', 'NAME_HOUSING_TYPE']
    for col in cat_cols:
        print(f"[{col}]", f"Number of Missing Values: {df[col].isnull().sum()}")

    # -------- Outlier Statistics --------
    print("\n\033[1;32m[Result of Data Cleaning (Outlier Statistics)]:\033[0m")
    num_cols = ['DAYS_EMPLOYED', 'CNT_CHILDREN', 'AMT_INCOME_TOTAL', 'CNT_FAM_MEMBERS']
    for col in num_cols:
        print(f"[{col}]", f"Minimum: {df[col].min()}, Maximum: {df[col].max()}")
print_processing_summary(merged_data)

# ------ Feature Engineering ------
merged_data['AGE'] = (-merged_data['DAYS_BIRTH'] // 365).astype(int)
merged_data['WORK_YEARS'] = (-merged_data['DAYS_EMPLOYED'] // 365).astype(int)  # Time Feature
merged_data['INCOME_PER_FAM'] = merged_data['AMT_INCOME_TOTAL'] / merged_data['CNT_FAM_MEMBERS']
merged_data['HAS_PHONE'] = ((merged_data['FLAG_PHONE'] == 'Y') | (merged_data['FLAG_WORK_PHONE'] == 'Y')).astype(int)  # Create new feature
merged_data['FLAG_OWN_CAR'] = merged_data['FLAG_OWN_CAR'].map({'Y': 1, 'N': 0})
merged_data['FLAG_OWN_REALTY'] = merged_data['FLAG_OWN_REALTY'].map({'Y': 1, 'N': 0})
cat_cols = ['CODE_GENDER', 'NAME_INCOME_TYPE', 'NAME_EDUCATION_TYPE',
            'OCCUPATION_TYPE', 'NAME_FAMILY_STATUS', 'NAME_HOUSING_TYPE']
merged_data = pd.get_dummies(merged_data, columns=cat_cols, drop_first=True)  # Categorical variable coding
drop_cols = ['DAYS_BIRTH','DAYS_EMPLOYED','FLAG_MOBIL','FLAG_WORK_PHONE','FLAG_PHONE','FLAG_EMAIL']
merged_data.drop(drop_cols, axis=1, inplace=True)  # Delete redundant features

# ------ Output the Data Structure after Cleaning ------
print("\n\033[1;32m[Example of Data Structure after Cleaning]:\033[0m")
print(merged_data.iloc[:11, :3])



# ========================= Step3: Model Construction and Interpretation =========================
print("\n\033[1;34m=== 3: Model Construction and Interpretation ===\033[0m")

# ------ Data Split ------
X = merged_data.drop(['ID','TARGET'], axis=1)
y = merged_data['TARGET']

# ------ Dealing Data Imbalance ------
X = X.astype(float)
smote = SMOTE(random_state=42)
X_res, y_res = smote.fit_resample(X, y)
print("\n\033[1;32m[Label Distribution before Oversampling]:\033[0m")
print(y.value_counts())
print("\n\033[1;32m[Label Distribution after Oversampling]:\033[0m")
print(pd.Series(y_res).value_counts())

# ------ Data Standardization ------
X_train, X_test, y_train, y_test = train_test_split(X_res, y_res, test_size=0.2, random_state=42)  # Divide data
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)  # Use the mean and variance of the training set
X_train_scaled = pd.DataFrame(X_train_scaled, columns=X_train.columns)
X_test_scaled = pd.DataFrame(X_test_scaled, columns=X_test.columns)

# ------ Construction of Hybrid Models ------
base_models = [
    ('logreg', LogisticRegression(max_iter=1000)),
    ('xgb', XGBClassifier(eval_metric='logloss',
                          max_depth=3,
                          reg_lambda=1.0,
                          subsample=0.8))]
stack_model = StackingClassifier(
    estimators=base_models,
    final_estimator=XGBClassifier(),
    stack_method='predict_proba')

# ------ Model Training ------
stack_model.fit(X_train_scaled, y_train)

# ------ Model Evaluation ------
y_pred = stack_model.predict(X_test_scaled)

# ------ Report ------
def custom_classification_report(y_true, y_pred):
    precision, recall, f1, _ = precision_recall_fscore_support(y_true, y_pred, average=None)
    accuracy = np.sum(y_true == y_pred) / len(y_true)
    report = (
        "                               precision    recall       f1-score\n"
        "Class 0 ('Good' Clients)    {:>8.2f}%    {:>8.2f}%    {:>8.2f}%\n"
        "Class 1 ('Bad' Clients)     {:>8.2f}%    {:>8.2f}%    {:>8.2f}%\n"
        "Accuracy                                              {:>8.2f}%\n"
        "Macro Average               {:>8.2f}%    {:>8.2f}%    {:>8.2f}%"
    ).format(
        precision[0] * 100, recall[0] * 100, f1[0] * 100,
        precision[1] * 100, recall[1] * 100, f1[1] * 100,
        accuracy * 100,
        np.mean(precision) * 100, np.mean(recall) * 100, np.mean(f1) * 100
    )
    return report
print("\n\033[1;32m[Report]:\033[0m")
print(custom_classification_report(y_test, y_pred))  # Format output

# --------- Visualization of Confusion Matrix ---------
cm = confusion_matrix(y_test, y_pred)
plt.figure()
sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', cbar=False)
plt.xlabel('Predicted Label')
plt.ylabel('True Label')
plt.title('Confusion Matrix')
plt.show()

# --------- SHAP Explanation ---------
explainer = shap.Explainer(stack_model.named_estimators_['xgb'], X_train_scaled)
shap_values = explainer(X_test_scaled)
plt.title("Feature Importance via SHAP Values")
shap.summary_plot(shap_values, X_test_scaled, feature_names=X.columns, show=False)
plt.gcf().set_size_inches(10, 6)
plt.show()



# ========================= Step4: Visual Display =========================
print("\n\033[1;34m=== 4. Visual Display ===\033[0m")

# ------ ROC Curve ------
y_proba = stack_model.predict_proba(X_test)[:,1]
fpr, tpr, _ = roc_curve(y_test, y_proba)
roc_auc = auc(fpr, tpr)
plt.figure()
plt.plot(fpr, tpr, color='darkorange', lw=2, label=f'ROC curve (AUC = {roc_auc:.2f})')
plt.plot([0, 1], [0, 1], color='navy', lw=2, linestyle='--')
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.title('ROC Curve')
plt.legend()
plt.show()

# ------ Confusion Matrix ------
cm = confusion_matrix(y_test, y_pred)
plt.figure()
sns.heatmap(cm, annot=True, fmt='d', cmap='Blues')
plt.xlabel('Predicted')
plt.ylabel('True')
plt.title('Confusion Matrix')
plt.show()
print("-------------- End of Project --------------")