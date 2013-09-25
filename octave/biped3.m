
load body_angles_1
load body_angles_2
load body_angles_3
load body_angles_4
load body_angles_5
load body_angles_6
load body_angles_7
load body_angles_8
load body_angles_9
load body_angles_10

a = [body_angles_1;
    body_angles_2;
    body_angles_3;
    body_angles_4;
    body_angles_5;
    body_angles_6;
    body_angles_7;
    body_angles_8;
    body_angles_9;
    body_angles_10];




%s1 = a(  a(:,5)==1 & a(:,6)==0 ,[1 2 7]);
%s1 = a(  (a(:,7)==1 | a(:,7)==0) & (a(:,5)==0 & a(:,6)==1), [1 2 8]);

s = a;
size(s)
scatter(mod(s(:,1)+pi,2*pi)-pi,mod(s(:,2)+pi,2*pi)-pi, [],s(:,7));
hold
plot([-pi pi],[pi -pi],'r');
axis equal
