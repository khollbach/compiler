#!/bin/bash

PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PASS_DIR="$PROJECT_DIR"/tests/passing
FAIL_DIR="$PROJECT_DIR"/tests/failing

# ensure
cd "$PROJECT_DIR"

# run passing tests, check for compiler success
echo "TESTING PASSING CASES:"

for entry in "$PASS_DIR"/*
do
    # run compiler silently
    ./RUNCOMPILER.sh "$entry" &> /dev/null

    # check for compiler success
    case $? in
        0) STATUS=PASS ;;
        1) STATUS=FAIL ;;
    esac

    # print test result
    echo "$(sed s:.*/::g <<< $entry): $STATUS"
done

# run passing tests, check for compiler success
echo "TESTING FAILING CASES:"

for entry in "$FAIL_DIR"/*
do
    # run compiler silently
    ./RUNCOMPILER.sh "$entry" &> /dev/null

    # check for compiler success
    case $? in
        1) STATUS=PASS ;;
        0) STATUS=FAIL ;;
    esac

    # print test result
    echo "$(sed s:.*/::g <<< $entry): $STATUS"
done
