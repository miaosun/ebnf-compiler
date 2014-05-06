Set name=%1
call jjtree %name%.jjt
call javacc %name%.jj
call javac *.java