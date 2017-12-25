function erreur = test_DNN(DNN, donnees, sortie)

out = entree_sortie_reseau(DNN,donnees);
N = zeros(size(out{end}));

for i=1:size(sortie,1)
    
[m,Ind] = max(out{end}(i,:));

N(i,Ind(1)) = 1;

end

erreur = abs(sortie-N);
erreur = mean(sum(erreur,2)/2);

end