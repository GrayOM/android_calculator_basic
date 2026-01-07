cmake_minimum_required(VERSION 3.22.1)

project("calculatingmachine")

# 여러 소스 파일 컴파일
add_library(calculatingmachine
        SHARED
        calculator.cpp      # 순수 C++ 계산 로직
        jni_bridge.cpp)     # JNI 레이어

# Android 로그 라이브러리 찾기
find_library(log-lib log)

# 라이브러리 연결
target_link_libraries(calculatingmachine
        ${log-lib})