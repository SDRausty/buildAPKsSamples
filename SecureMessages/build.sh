#!/bin/bash

##################################################################
#  CONFIGURE IT FOR YOUR SYSTEM !
##################################################################

export JAVABIN=/usr/lib/jvm/java-6-sun-1.6.0.24/bin/
export ANDROIDSDK=~/INSTALL/android-sdk-linux_x86
export PATH=$PATH:~/INSTALL/android-sdk-linux_x86/tools:


##################################################################
#  PREPARE ENVIRONMENT
##################################################################

CD=`pwd`

rm -r bin
rm -r gen
mkdir bin
mkdir gen


##################################################################
#  SET VARS
##################################################################

# Set your application name
APP_NAME=SecureSms

# Define minimal Android revision
ANDROID_REV=android-8

# Define aapt add command
ANDROID_AAPT_ADD="$ANDROIDSDK/platforms/$ANDROID_REV/tools/aapt add"

# Define aapt pack and generate resources command
ANDROID_AAPT_PACK="$ANDROIDSDK/platforms/$ANDROID_REV/tools/aapt package -v -f -I $ANDROIDSDK/platforms/$ANDROID_REV/android.jar"

# Define class file generator command
ANDROID_DX="$ANDROIDSDK/platform-tools/dx --dex"

# Define Java compiler command
JAVAC="$JAVABIN/javac -classpath $ANDROIDSDK/platforms/$ANDROID_REV/android.jar"
JAVAC_BUILD="$JAVAC -sourcepath src -sourcepath gen -d bin"


##################################################################
#  PROCESS
##################################################################

# Generate R class and pack resources and assets into resources.ap_ file
$ANDROID_AAPT_PACK -M "AndroidManifest.xml" -A "assets" -S "res" -m -J "gen" -F "bin/resources.ap_"

# Compile sources. All *.class files will be put into the bin folder
$JAVAC_BUILD src/org/secure/sms/*.java

# Generate dex files with compiled Java classes
$ANDROID_DX --output="$CD/bin/classes.dex" $CD/bin

# Recources file need to be copied. This is needed for signing.
mv "$CD/bin/resources.ap_" "$CD/bin/$APP_NAME.ap_"

# Add generated classes.dex file into application package
$ANDROID_AAPT_ADD "$CD/bin/$APP_NAME.ap_" "$CD/bin/classes.dex"

# Create signed Android application from *.ap_ file. Output and Input files must be different.
"$JAVABIN/jarsigner" -keystore "$CD/keystore/my-okalash-key.keystore" -storepass "123456" -keypass "123456" -signedjar "$CD/bin/$APP_NAME.apk" "$CD/bin/$APP_NAME.ap_" "okalash-android-key"

# Delete temp file
rm "bin/$APP_NAME.ap_"

