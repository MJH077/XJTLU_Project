# Used AI for programming
import json
import random
import time

# Record the program start time
start_time = time.time()


# Read instances of the bin packing problem from a JSON file
def read_bin_packing_instances(json_file_path):
    try:
        with open(json_file_path, 'r') as file:
            instances = json.load(file)
    except FileNotFoundError as e:
        # Print an error message that the file does not exist
        print(f"File not found: {json_file_path}. Error: {e}")
        instances = []
    except json.JSONDecodeError as e:
        # Print a json parsing error message
        print(f"Error decoding JSON from the file. Error: {e}")
        instances = []
    except Exception as e:
        # Print error messages for other exceptions
        print(f"An error occurred: {e}")
        instances = []
    return instances


# First Fit Decreasing (FFD) algorithm for bin packing
def first_fit_decreasing(items, capacity):
    sorted_items = sorted(items, reverse=True)
    bins = []
    for item in sorted_items:
        placed = False
        for bin in bins:
            if sum(bin) + item <= capacity:
                bin.append(item)
                placed = True
                break
        if not placed:
            # Try to merge with existing bins if no suitable bin found
            merged = False
            for bin in bins:
                space_left = capacity - sum(bin)
                if space_left >= item:
                    bin.append(item)
                    merged = True
                    break
            if not merged:
                # Try to merge with adjacent bins and optimize merge strategy
                for i in range(len(bins) - 1):
                    if sum(bins[i]) + sum(bins[i + 1]) <= capacity:
                        bins[i] += bins[i + 1]
                        del bins[i + 1]
                        bins[i].append(item)
                        merged = True
                        break
            if not merged:
                bins.append([item])
    return bins


# Randomly search the algorithm and arrange the items in a random order
def random_search_fit(items, capacity, fit_fun, iterations=10):
    best_solution = None
    min_bins = float('inf')
    for _ in range(iterations):
        # Shuffle the order of items randomly
        random.shuffle(items)
        # Find a solution using the given packing algorithm
        current_solution = fit_fun(items, capacity)
        if len(current_solution) < min_bins:
            min_bins = len(current_solution)
            best_solution = current_solution
    return best_solution


def solve_instances(instances, fit_function, solver_function, output_file_path):
    total_bins = 0
    output_json = {'time': 0, 'res': []}
    for ins in instances:
        solution = solver_function(ins['items'], ins['capacity'], fit_function)
        # Save and print output
        output_json['res'].append({
            'name': ins['name'],
            'capacity': ins['capacity'],
            'items': ins['items'],
            'solution': solution
        })
        print('Instance:', ins['name'], "\t", "Minimum number of bins used:", len(solution))
        total_bins += len(solution)
    with open(output_file_path, 'w+') as f:
        # Calculate program run time
        output_json['time'] = (time.time() - start_time) / 60
        # Write the results to a json file
        json.dump(output_json, f, indent=4)
    # Total number of boxes printed
    print("Total Bins:", total_bins)


if __name__ == "__main__":
    # Example usage
    random.seed(0)
    json_file_path = 'CW_ins.json'
    instances = read_bin_packing_instances(json_file_path)
    solve_instances(instances, first_fit_decreasing, random_search_fit, '2254767_Jinghan_Ma.json')
