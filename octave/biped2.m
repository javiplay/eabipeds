load body_angles;
load joint_pos;
load action_angles;


plot(joint_pos);
figure;
plot(joint_pos(2:length(joint_pos))-joint_pos(1:length(joint_pos)-1));


figure;
hold on
plot(body_angles(:,1),body_angles(:,2));
scatter(body_angles(:,1),body_angles(:,2));
scatter(action_angles(:,1),action_angles(:,2),'red');
hold off;


