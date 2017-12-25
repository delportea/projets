function [ mat ] = lire_alpha_digit( num,dat )

n=length(num);
L=n*size(dat,2);
mat=zeros(L,size(dat{1,1},1)*size(dat{1,1},2));
pas=0;

for k=1:n
    
    for i=1:size(dat,2)

       %datmat=cell2mat(dat(k,i));
       pas=pas+1;
       mat(pas,:)=reshape(dat{num(k),i},1,size(dat{num(k),i},1)*size(dat{num(k),i},2));
              
    end
end


end

