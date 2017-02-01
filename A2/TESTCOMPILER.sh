#!/bin/bash

PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PASS_DIR="$PROJECT_DIR"/tests/passing
FAIL_DIR="$PROJECT_DIR"/tests/failing

echo $PASS_DIR
echo $FAIL_DIR
