function [ image ] = generer_image_DBN( DNN, nb_iter_Gibbs, nb_images, nb_couches, tailleimage)


for i=1:nb_images
    
    vis=rand(1,size(DNN{end-2}.a,2));
    vis=(vis>=1/2);
    
    for k=1:nb_iter_Gibbs
        
        hid=entree_sortie_RBM(DNN{end-1},vis);
        samplehid=(rand(size(hid))<hid);
        vis=sortie_entree_RBM(DNN{end-1},samplehid);
        samplevis=(rand(size(vis))<vis);
        
    end
    
    for j=(length(DNN)-2):-1:1
        
        vis=sortie_entree_RBM(DNN{j},samplevis);
        samplevis=(rand(size(vis))<vis);
        
    end
        
    image=reshape(samplevis,tailleimage(1),tailleimage(2));
    figure()
    imshow(image)    
    
end


end

