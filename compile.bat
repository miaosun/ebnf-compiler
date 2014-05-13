if "%1"== "" Set name=Ebnf
if NOT "%1" == "" Set name=%1
call jjtree %name%.jjt
call javacc %name%.jj
call javac *.java