function [ DNN ] = train_DBN ( DNN, nb_iter_grad, learning_rate, batch_size, mat )

for i=1:(length(DNN)-1)

    DNN{i}=train_RBM(DNN{i}, nb_iter_grad, learning_rate, batch_size, mat);
    mat=entree_sortie_RBM(DNN{i},mat);
end
end

