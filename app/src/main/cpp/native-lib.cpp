//
// Created by zhout on 2018/4/9.
//
#include <jni.h>
#include <string>

std::string sum(int i, int i1);

extern "C"
JNIEXPORT jstring JNICALL

Java_com_zhoutianchu_framework_activity_MainActivity_stringFromJNI(JNIEnv *env, jobject instance,
                                                                   jstring s_) {
    const char *s = env->GetStringUTFChars(s_, false);

    std::string returnValue="hello,";

    returnValue.append(s);

    returnValue.append(sum(0,1));

    return env->NewStringUTF(returnValue.c_str());

}

std::string sum(int i, int i1) {
    return "this sum func";
}
