function [ RBM ] = train_RBM( RBM, nb_iter_grad, learning_rate, batch_size, mat)
 
n=size(mat,1);
r=randperm(n);
mat=mat(r,:);

for l=1:nb_iter_grad
    
     for b=1:batch_size:n
         
         vis1=mat(b:min(b+batch_size-1,n),:);
         hid1=entree_sortie_RBM(RBM,vis1);
         
         samplehid1=(rand(size(vis1,1),size(hid1,2))<hid1);
         
         vis2=sortie_entree_RBM(RBM,samplehid1);
         samplevis2=(rand(size(vis2,1),size(vis2,2))<vis2);
         
         hid2=entree_sortie_RBM(RBM,samplevis2);
         
         pos=(hid1)'*vis1;
         neg=hid2'*samplevis2;
         dw=(pos-neg)';
         da=sum(vis1-samplevis2,1);
         db=sum(hid1-hid2,1);
         
         RBM.w=RBM.w+(learning_rate/batch_size)*dw;
         RBM.a=RBM.a+(learning_rate/batch_size)*da;
         RBM.b=RBM.b+(learning_rate/batch_size)*db;
        
     end 
     
hid=entree_sortie_RBM(RBM,mat);
donnees_reconstruit=sortie_entree_RBM(RBM,hid);
erreur=sum(sum(power(mat-donnees_reconstruit,2))/numel(mat));
l
fprintf('iteration gradient: %d, erreur= %d \n',l,erreur);
V1=[0;0;1000];
plot(V1,erreur,'k.');
end

