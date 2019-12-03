FURY_VERSION=0.7.14
LIB=quarantine

$(LIB).jar: compile
	mkdir -p bin
	.local/fury/bin/fury build save --https --output linear --fat-jar --dir bin

compile: .local/fury/bin/fury
	.local/fury/bin/fury config set --theme full
	.local/fury/bin/fury layer extract -f ${LIB}.fury
	.local/fury/bin/fury build run --https --output linear || .local/fury/bin/fury permission grant -P b7a
	.local/fury/bin/fury build run --https --output linear

install.sh:
	curl http://downloads.furore.dev/fury-${FURY_VERSION}.sh > install.sh

.local/fury/bin/fury: install.sh
	chmod +x install.sh
	./install.sh .local/fury

.PHONY: compile
