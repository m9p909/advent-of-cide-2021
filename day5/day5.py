
with open('input.txt', 'r') as file:
    raw = file.read().split("\n")
    max_value = 1000
    x = []
    for i in range(max_value):
        x.append([])
        for j in range(max_value):
            x[i].append(0)
    for line in raw:
        numbers = line.split(" -> ")
        results = []
        for value in numbers:
            coords = value.split(",")
            if len(coords) > 1:
                results.append({"x": coords[0],
                                "y": coords[1]})
        print(results)
