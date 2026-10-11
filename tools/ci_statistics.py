#!/usr/bin/python


import json

if __name__ == '__main__':
    with open("statistics.json") as fh:
        data = json.load(fh)

    _, v = next(iter(data.items()))
    keys = list(v.keys())
    keys.sort()

    table = [("Method", *keys)]
    for m, d in data.items():
        seq = [str(d[x]) for x in keys]
        table.append((m, *seq))

    column_sizes = [max(len(row[c]) for row in table)
                    for c in range(0, 1+len(keys))]

    def ljustrow(row, char=" "):
        return (" " + txt.ljust(sz, char) + " "
                for sz, txt in zip(column_sizes, row))

    print("|" + ('|'.join(ljustrow(table[0]))) + "|")
    print("|" + ('|'.join(ljustrow(("" for x in range(len(keys)+1)), '-'))) + "|")

    for row in table[1:]:
        print("|" + ('|'.join(ljustrow(row))) + "|")
