#!/usr/bin/python
import sys


def main(argv):
    file = open(argv[0])
    line = file.readline()
    while line != '':
        if not '<!--' in line:
            print(line.strip(), end='')
        line = file.readline()


if __name__ == '__main__':
    main(sys.argv[1:])
