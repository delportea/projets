function [ entree ] = sortie_entree_RBM(RBM, donnees )

sortie = donnees*(RBM.w)' + repmat(RBM.a,size(donnees,1),1);
entree = 1./(1+exp(-sortie));

end

