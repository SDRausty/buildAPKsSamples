#!/bin/env bash # Copyright 2019 (c) all rights reserved 
# by BuildAPKs https://buildapks.github.io
#####################################################################
set -Eeuo pipefail
shopt -s nullglob globstar

_SMATRPERROR_() { # run on script error
	local RV="$?"
	echo "$RV" ma.bash
	printf "\\e[?25h\\n\\e[1;48;5;138mBuildAPKs %s ERROR:  Generated script error %s near or at line number %s by \`%s\`!\\e[0m\\n" "${PWD##*/}" "${1:-UNDEF}" "${2:-LINENO}" "${3:-BASH_COMMAND}"
	exit 147
}

_SMATRPEXIT_() { # run on exit
	printf "\\e[?25h\\e[0m"
	set +Eeuo pipefail 
	exit
}

_SMATRPSIGNAL_() { # run on signal
	local RV="$?"
	printf "\\e[?25h\\e[1;7;38;5;0mBuildAPKs %s WARNING:  Signal %s received!\\e[0m\\n" "ma.bash" "$RV"
 	exit 148 
}

_SMATRPQUIT_() { # run on quit
	local RV="$?"
	printf "\\e[?25h\\e[1;7;38;5;0mBuildAPKs %s WARNING:  Quit signal %s received!\\e[0m\\n" "ma.bash" "$RV"
 	exit 149 
}

trap '_SMATRPERROR_ $? $LINENO $BASH_COMMAND' ERR 
trap _SMATRPEXIT_ EXIT
trap _SMATRPSIGNAL_ HUP INT TERM 
trap _SMATRPQUIT_ QUIT 

cd "$JDR"
_AT_ burhanaras/AndroidRepo 0ee086b47413b690435642c1b16bc81b919245e2
_AT_ burhanaras/MvApp2SD 5259ae87e34ae4e6548000251315ebf0ba7bc455
_AT_ mr-dddalkilanny/tree-view-list-android 798fc10fb7a584b02533a601f0b83424e8bf5775
_AT_ rorist/android-alarm-button 72fd5e1f0e570387597d82bce06dcf3715283443
_AT_ rorist/Cowsay-android c14df9e362aacb68ba99fec24d3d7dce669e043b
_AT_ six999/android-vitosha-poker-odds b98f2052f65f81477f3f8589e9836549f45a1c5e
_AT_ StevenByle/Android-Density-Independence-Demo 82ce51ead3ba4f1ca37256091ff61604c781b319
(cd StevenByle/Android-Density-Independence-Demo/StevenByle-Android-Density-Independence-Demo-82ce51e ; mv 'Android Density Independence' AndroidDensityIndependence ; cd "$JDR") || printf "%s\\n" "Cannot process directory; Continuing..."
_AT_ twuster/Rubix 51be3c6c808ff95d0533ad77deac088c2af49f80
_AT_ VelbazhdSoftwareLLC/android-vitosha-poker-odds 182a79416634154cbaf771926410e02bec849279

# ma.bash OEF
