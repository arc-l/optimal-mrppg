Source code used in the following work 

J. Yu and S. M. LaValle. Planning Optimal Paths for Multiple Robots on Graphs. 
In 2013 IEEE International Conference on Robotics and Automation (ICRA 2013).

Latest manuscript: https://arxiv.org/pdf/1204.3830v4.pdf

##Description

Here you will find the sources/binaries of the code in the paper containing a simple API. Due to multiple requests, I am releasing the source code in a rather raw form without documentation so use with discretion. The source is slightly newer than the binary release. To use the binary/API, you will need an x64 machine with 4-8GB memory, a 64bit JRE/JDK. You will also need to have an appropriate 64bit version of Gurobi installed (an academic license is free to obtain). Then you need to find the gurobi.jar and make sure that the Gurobi binaries are on your system path. 

For direct use, grab Main.java and mapp.jar under the bytecode folder. instances.zip contains examples used in the paper and the source folder contains all source files for those who would like 
to tinker more.

OK, assuming you have everything setup correctly, to try a randomly created 16-puzzle, you can run from command line the following:

>java -cp mapp.jar;gurobi.jar projects.multipath.advanced.Main 1

You should get an output similar to the following:

>Solving a randomly generated 16-puzzle, generally solvable in 60 seconds on an intel dual core x64 machine. 

>Start and goal vertices: 16 pairs
>0(0,0) 1(1,0) 2(2,0) 3(3,0) 4(0,1) 5(1,1) 6(2,1) 7(3,1) 8(0,2) 9(1,2) 10(2,2) 11(3,2) 12(0,3) 13(1,3) 14(2,3) 15(3,3)
>5(1,1) 0(0,0) 1(1,0) 9(1,2) 3(3,0) 11(3,2) 7(3,1) 8(0,2) 13(1,3) 12(0,3) 6(2,1) 15(3,3) 4(0,1) 2(2,0) 10(2,2) 14(2,3)

>Solution path set:

>Agent 0: 0:0(0,0) 1:4(0,1) 2:8(0,2) 3:8(0,2) 4:4(0,1) 5:5(1,1)
>Agent 1: 0:1(1,0) 1:0(0,0) 2:4(0,1) 3:4(0,1) 4:0(0,0) 5:0(0,0)
>Agent 2: 0:2(2,0) 1:1(1,0) 2:0(0,0) 3:0(0,0) 4:1(1,0) 5:1(1,0)
>Agent 3: 0:3(3,0) 1:3(3,0) 2:7(3,1) 3:11(3,2) 4:10(2,2) 5:9(1,2)
>Agent 4: 0:4(0,1) 1:5(1,1) 2:1(1,0) 3:2(2,0) 4:3(3,0) 5:3(3,0)
>Agent 5: 0:5(1,1) 1:6(2,1) 2:2(2,0) 3:3(3,0) 4:7(3,1) 5:11(3,2)
>Agent 6: 0:6(2,1) 1:2(2,0) 2:3(3,0) 3:7(3,1) 4:6(2,1) 5:7(3,1)
>Agent 7: 0:7(3,1) 1:7(3,1) 2:6(2,1) 3:5(1,1) 4:9(1,2) 5:8(0,2)
>Agent 8: 0:8(0,2) 1:12(0,3) 2:13(1,3) 3:13(1,3) 4:13(1,3) 5:13(1,3)
>Agent 9: 0:9(1,2) 1:8(0,2) 2:12(0,3) 3:12(0,3) 4:12(0,3) 5:12(0,3)
>Agent 10: 0:10(2,2) 1:10(2,2) 2:10(2,2) 3:6(2,1) 4:5(1,1) 5:6(2,1)
>Agent 11: 0:11(3,2) 1:11(3,2) 2:11(3,2) 3:15(3,3) 4:11(3,2) 5:15(3,3)
>Agent 12: 0:12(0,3) 1:13(1,3) 2:9(1,2) 3:9(1,2) 4:8(0,2) 5:4(0,1)
>Agent 13: 0:13(1,3) 1:9(1,2) 2:5(1,1) 3:1(1,0) 4:2(2,0) 5:2(2,0)
>Agent 14: 0:14(2,3) 1:14(2,3) 2:14(2,3) 3:10(2,2) 4:14(2,3) 5:10(2,2)
>Agent 15: 0:15(3,3) 1:15(3,3) 2:15(3,3) 3:14(2,3) 4:15(3,3) 5:14(2,3)


The "Start and goal vertices" part tells you what the starts/goals are. You may notice that the start is actually the row major ordering of the agents and the goals are randomly selected. In the paper, it was the other way -- the goals follow the row major ordering. The "Solution path set" part is the solution, with each line corresponding to one agent. Each line is essentially a path with the format [step:vertex number:(x,y)]. Note that the ":(x,y)" part may be empty, depending on how the graph is constructed. So the above is a 5-step path for 16 agents. 

To create and solve a randome problem with 15 start/goal pairs on a 15x15 grid with 20% vertices removed to simulate obstacles, run

>java -cp mapp.jar;gurobi.jar projects.multipath.advanced.Main 2

You will get an out put similar to the following:

>Solving a randomly generated multi-agent path planning problem on a 15x15 grid with 20% obstacles and 15 agents, with 60 second time limit.

>Printing adjacency list of the graph:
>0: 12,
>1: 2, 14,
>2: 1, 3, 15,
>3: 2, 16,
>4: 5, 19,
>5: 4, 6,
>6: 5, 7, 20,
>7: 6, 8, 21,
>8: 7, 9,
>9: 8, 10, 22,
>10: 9, 11, 23,
>......
>......
>......
>172: 163, 171,
>173: 164, 174,
>174: 165, 173, 175,
>175: 166, 174, 176,
>176: 167, 175, 177,
>177: 168, 176, 178,
>178: 169, 177, 179,
>179: 178,

>Solution path set:

>Agent 0: 0:69(8,5) 1:70(9,5) 2:70(9,5) 3:71(10,5) 4:71(10,5) 5:70(9,5) 6:70(9,5) 7:70(9,5) 8:70(9,5) 9:70(9,5) 10:70(9,5) 11:70(9,5) 12:70(9,5) 13:70(9,5) 14:70(9,5) 15:83(9,6) 16:94(9,7) 17:95(10,7) 18:106(10,8) 19:107(11,8) 20:117(11,9) 21:129(11,10) 22:130(12,10)
>Agent 1: 0:31(8,2) 1:44(8,3) 2:45(9,3) 3:46(10,3) 4:46(10,3) 5:46(10,3) 6:46(10,3) 7:46(10,3) 8:46(10,3) 9:46(10,3) 10:46(10,3) 11:46(10,3) 12:46(10,3) 13:46(10,3) 14:46(10,3) 15:46(10,3) 16:46(10,3) 17:45(9,3) 18:60(9,4) 19:70(9,5) 20:83(9,6) 21:94(9,7) 22:93(8,7)
>Agent 2: 0:175(10,14) 1:166(10,13) 2:153(10,12) 3:152(9,12) 4:141(9,11) 5:128(9,10) 6:127(8,10) 7:115(8,9) 8:114(7,9) 9:103(7,8) 10:102(6,8) 11:92(6,7) 12:91(5,7) 13:90(4,7) 14:79(4,6) 15:68(4,5) 16:67(3,5) 17:54(3,4) 18:53(2,4) 19:52(1,4)20:37(1,3) 21:36(0,3) 22:25(0,2)
>Agent 3: 0:8(11,0) 1:7(10,0) 2:6(9,0) 3:5(8,0) 4:4(7,0) 5:19(7,1) 6:18(6,1) 7:17(5,1) 8:16(4,1) 9:15(3,1) 10:14(2,1) 11:13(1,1) 12:12(0,1) 13:0(0,0) 14:0(0,0) 15:12(0,1) 16:25(0,2) 17:36(0,3) 18:37(1,3) 19:36(0,3) 20:25(0,2) 21:12(0,1) 22:0(0,0)
>Agent 4: 0:103(7,8) 1:104(8,8) 2:105(9,8) 3:106(10,8) 4:106(10,8) 5:106(10,8) 6:106(10,8) 7:106(10,8) 8:106(10,8) 9:106(10,8) 10:106(10,8) 11:106(10,8) 12:106(10,8) 13:105(9,8) 14:105(9,8) 15:105(9,8) 16:116(9,9) 17:128(9,10) 18:128(9,10) 19:141(9,11) 20:152(9,12) 21:165(9,13) 22:166(10,13)
>Agent 5: 0:127(8,10) 1:115(8,9) 2:116(9,9) 3:128(9,10) 4:128(9,10) 5:116(9,9) 6:128(9,10) 7:141(9,11) 8:128(9,10) 9:141(9,11) 10:141(9,11) 11:142(10,11) 12:141(9,11) 13:140(8,11) 14:139(7,11) 15:138(6,11) 16:137(5,11) 17:150(5,12) 18:149(4,12) 19:148(3,12) 20:147(2,12) 21:146(1,12) 22:145(0,12)
>Agent 6: 0:174(9,14) 1:173(8,14) 2:164(8,13) 3:173(8,14) 4:174(9,14) 5:174(9,14) 6:174(9,14) 7:174(9,14) 8:174(9,14) 9:174(9,14) 10:174(9,14) 11:165(9,13) 12:152(9,12) 13:141(9,11) 14:141(9,11) 15:140(8,11) 16:139(7,11) 17:138(6,11) 18:137(5,11) 19:150(5,12) 20:149(4,12) 21:148(3,12) 22:161(3,13)
>Agent 7: 0:119(14,9) 1:132(14,10) 2:132(14,10) 3:132(14,10) 4:132(14,10) 5:132(14,10) 6:132(14,10) 7:132(14,10) 8:132(14,10) 9:132(14,10) 10:132(14,10) 11:132(14,10) 12:132(14,10) 13:132(14,10) 14:131(13,10) 15:118(13,9) 16:119(14,9) 17:119(14,9) 18:119(14,9) 19:110(14,8) 20:109(13,8) 21:108(12,8) 22:107(11,8)
>Agent 8: 0:66(2,5) 1:53(2,4) 2:54(3,4) 3:39(3,3) 4:40(4,3) 5:40(4,3) 6:40(4,3) 7:40(4,3) 8:40(4,3) 9:40(4,3) 10:55(4,4) 11:56(5,4) 12:55(4,4) 13:54(3,4) 14:39(3,3) 15:54(3,4) 16:53(2,4) 17:53(2,4) 18:38(2,3) 19:39(3,3) 20:40(4,3) 21:41(5,3) 22:42(6,3)
>Agent 9: 0:22(12,1) 1:23(13,1) 2:24(14,1) 3:24(14,1) 4:24(14,1) 5:24(14,1) 6:24(14,1) 7:35(14,2) 8:35(14,2) 9:34(13,2) 10:33(12,2) 11:22(12,1) 12:33(12,2) 13:33(12,2) 14:34(13,2) 15:35(14,2) 16:24(14,1) 17:24(14,1) 18:23(13,1) 19:34(13,2) 20:34(13,2) 21:33(12,2) 22:34(13,2)
>Agent 10: 0:140(8,11) 1:139(7,11) 2:126(7,10) 3:126(7,10) 4:126(7,10) 5:126(7,10) 6:126(7,10) 7:126(7,10) 8:126(7,10) 9:126(7,10) 10:126(7,10) 11:127(8,10) 12:128(9,10) 13:128(9,10) 14:127(8,10) 15:126(7,10) 16:126(7,10) 17:127(8,10) 18:127(8,10) 19:126(7,10) 20:114(7,9) 21:115(8,9) 22:104(8,8)
>Agent 11: 0:167(11,13) 1:168(12,13) 2:177(12,14) 3:168(12,13) 4:168(12,13) 5:168(12,13) 6:167(11,13) 7:154(11,12) 8:154(11,12) 9:153(10,12) 10:142(10,11) 11:143(11,11) 12:154(11,12) 13:153(10,12) 14:153(10,12) 15:142(10,11) 16:142(10,11) 17:153(10,12) 18:153(10,12) 19:142(10,11) 20:141(9,11) 21:141(9,11) 22:128(9,10)
>Agent 12: 0:123(3,10) 1:123(3,10) 2:122(2,10) 3:135(2,11) 4:147(2,12) 5:147(2,12) 6:147(2,12) 7:160(2,13) 8:147(2,12) 9:146(1,12) 10:145(0,12) 11:146(1,12) 12:145(0,12) 13:146(1,12) 14:134(1,11) 15:133(0,11) 16:134(1,11) 17:135(2,11) 18:136(3,11) 19:136(3,11) 20:135(2,11) 21:135(2,11) 22:147(2,12)
>Agent 13: 0:131(13,10) 1:130(12,10) 2:129(11,10) 3:143(11,11) 4:142(10,11) 5:141(9,11) 6:140(8,11) 7:139(7,11) 8:138(6,11) 9:137(5,11) 10:125(5,10) 11:124(4,10) 12:123(3,10) 13:122(2,10) 14:121(1,10) 15:112(1,9) 16:99(1,8) 17:100(2,8) 18:88(2,7) 19:89(3,7) 20:90(4,7) 21:79(4,6) 22:80(5,6)
>Agent 14: 0:25(0,2) 1:12(0,1) 2:13(1,1) 3:13(1,1) 4:12(0,1) 5:12(0,1) 6:12(0,1)7:12(0,1) 8:12(0,1) 9:12(0,1) 10:12(0,1) 11:12(0,1) 12:25(0,2) 13:36(0,3) 14:51(0,4) 15:65(0,5) 16:76(0,6) 17:77(1,6) 18:87(1,7) 19:88(2,7) 20:89(3,7) 21:90(4,7) 22:91(5,7)


From the adjacency list, the graph can be reconstructed. To load a problem from a file and solve it, run

> java -cp mapp.jar;gurobi.jar projects.multipath.advanced.Main 4 [filename]

If you coded in Java before, reading Main.java will give you all you need to use what is available on this page. In particular, although the examples were not saved in a readable format, it is fairly easy to load a problem and write the graph into a readable adjacency list format.
