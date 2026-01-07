#include "calculator.h"
#include <stdexcept>

namespace Calculator {

    // 덧셈 구현
    double add(double a, double b) {
        return a + b;
    }

    // 뺄셈 구현
    double subtract(double a, double b) {
        return a - b;
    }

    // 곱셈 구현
    double multiply(double a, double b) {
        return a * b;
    }

    // 나눗셈 구현 (예외 처리)
    double divide(double a, double b) {
        if (b == 0.0) {
            throw std::invalid_argument("Division by zero");
        }
        return a / b;
    }

}  // namespace Calculator