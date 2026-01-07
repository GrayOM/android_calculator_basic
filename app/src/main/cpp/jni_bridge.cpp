#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "NativeCalculator"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

// 덧셈 (패키지명 수정: calculatingmachine)
extern "C" JNIEXPORT jdouble JNICALL
Java_com_example_calculatingmachine_MainActivity_add(
        JNIEnv* env,
        jobject /* this */,
        jdouble a,
        jdouble b) {
    LOGD("add() called: a=%f, b=%f", a, b);
    jdouble result = a + b;
    LOGD("add() result: %f", result);
    return result;
}

// 뺄셈
extern "C" JNIEXPORT jdouble JNICALL
Java_com_example_calculatingmachine_MainActivity_subtract(
        JNIEnv* env,
        jobject /* this */,
        jdouble a,
        jdouble b) {
    LOGD("subtract() called: a=%f, b=%f", a, b);
    jdouble result = a - b;
    LOGD("subtract() result: %f", result);
    return result;
}

// 곱셈
extern "C" JNIEXPORT jdouble JNICALL
Java_com_example_calculatingmachine_MainActivity_multiply(
        JNIEnv* env,
        jobject /* this */,
        jdouble a,
        jdouble b) {
    LOGD("multiply() called: a=%f, b=%f", a, b);
    jdouble result = a * b;
    LOGD("multiply() result: %f", result);
    return result;
}

// 나눗셈 (에러 처리 포함)
extern "C" JNIEXPORT jdouble JNICALL
Java_com_example_calculatingmachine_MainActivity_divide(
        JNIEnv* env,
        jobject /* this */,
        jdouble a,
        jdouble b) {

    LOGD("divide() called: a=%f, b=%f", a, b);

    // 0으로 나누기 체크
    if (b == 0.0) {
        LOGD("Division by zero detected!");
        jclass exceptionClass = env->FindClass("java/lang/ArithmeticException");
        env->ThrowNew(exceptionClass, "Division by zero");
        return 0.0;
    }

    jdouble result = a / b;
    LOGD("divide() result: %f", result);
    return result;
}