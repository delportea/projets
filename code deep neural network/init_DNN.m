function [ DNN ] = init_DNN( config )

nb_couches=length(config)-1;
for i = 1:nb_couches
    DNN{i}.a=zeros(1,config(i));
    DNN{i}.b=zeros(1,config(i+1));
    DNN{i}.w=randn(config(i),config(i+1))*0.1;

end
end

