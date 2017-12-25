function [sortie] = entree_sortie_RBM( RBM, donnees )

entree = donnees*RBM.w+repmat(RBM.b,size(donnees,1),1);
sortie = 1./(1+exp(-entree));
end

