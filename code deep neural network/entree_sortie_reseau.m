function [ tab ] = entree_sortie_reseau(DNN, donnees )

num=length(DNN);

for i=1:num-1
    entree=entree_sortie_RBM(DNN{i},donnees);
    donnees=entree;
    tab{i}=donnees;
end

proba_sortie=calcul_softmax(DNN{num},donnees);
tab{end+1}=proba_sortie;