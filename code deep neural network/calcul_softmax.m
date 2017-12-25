function prob = calcul_softmax(RBM, donnees )

 a=exp(donnees*RBM.w+repmat(RBM.b,size(donnees,1),1));
 b=repmat(sum(a,2),1,size(a,2));
 prob=a./b;
 
end

