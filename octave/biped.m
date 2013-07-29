
hold off;
load body_angles;
states = size(body_angles)(1);


h = plot(body_angles(1,1),body_angles(1,2));
axis("equal");
f = 1;
axis([-f*pi f*pi -f*pi f*pi]); 
for i_=1:states
    set(h, 'XData', body_angles(1:i_,1));
    set(h, 'YData', body_angles(1:i_,2));
    pause(0.001);   
end

