echo on

SET PREV_PATH=%CD%
cd /d %0\..

REM Clear bin folder
rmdir "bin" /S /Q
rmdir "gen" /S /Q
mkdir "bin" || goto EXIT
mkdir "gen" || goto EXIT

REM Set your application name
SET APP_NAME=SecureSms

REM Define minimal Android revision
SET ANDROID_REV=android-8

REM Define aapt add command
SET ANDROID_AAPT_ADD="%ANDROID-SDK%\platforms\%ANDROID_REV%\tools\aapt.exe" add

REM Define aapt pack and generate resources command
SET ANDROID_AAPT_PACK="%ANDROID-SDK%\platforms\%ANDROID_REV%\tools\aapt.exe" package -v -f -I "%ANDROID-SDK%\platforms\%ANDROID_REV%\android.jar"

REM Define class file generator command
SET ANDROID_DX="%ANDROID-SDK%\platform-tools\dx.bat" --dex

REM Define Java compiler command
SET JAVAC="%JAVABIN%\javac.exe" -classpath "%ANDROID-SDK%\platforms\%ANDROID_REV%\android.jar"
SET JAVAC_BUILD=%JAVAC% -sourcepath "src;gen" -d "bin"

REM Generate R class and pack resources and assets into resources.ap_ file
call %ANDROID_AAPT_PACK% -M "AndroidManifest.xml" -A "assets" -S "res" -m -J "gen" -F "bin\resources.ap_" || goto EXIT

REM Compile sources. All *.class files will be put into the bin folder
call %JAVAC_BUILD% src\org\secure\sms\*.java || goto EXIT

REM Generate dex files with compiled Java classes
call %ANDROID_DX% --output="%CD%\bin\classes.dex" %CD%\bin || goto EXIT

REM Recources file need to be copied. This is needed for signing.
copy "%CD%\bin\resources.ap_" "%CD%\bin\%APP_NAME%.ap_" || goto EXIT

REM Add generated classes.dex file into application package
call %ANDROID_AAPT_ADD% "%CD%\bin\%APP_NAME%.ap_" "%CD%\bin\classes.dex" || goto EXIT

REM Create signed Android application from *.ap_ file. Output and Input files must be different.
call "%JAVABIN%\jarsigner" -keystore "%CD%\keystore\my-release-key.keystore" -storepass "password" -keypass "password" -signedjar "%CD%\bin\%APP_NAME%.apk" "%CD%\bin\%APP_NAME%.ap_" "alias_name" || goto EXIT

REM Delete temp file
del "bin\%APP_NAME%.ap_"

:EXIT
cd "%PREV_PATH%"
ENDLOCAL
exit /b %ERRORLEVEL%
