function [ image ] = generer_image_RBM(RBM,nb_iter_Gibbs,nb_images)

p = size(RBM.w,1);
q = size(RBM.w,2);

for i=1:nb_images
    
    tirage = rand(1,p);
    tirage = (tirage>=1/2);
    
    for j=1:nb_iter_Gibbs
        h=entree_sortie_RBM(RBM, tirage);
        tirage=(rand(size(h)))<h;
        v=sortie_entree_RBM(RBM ,tirage);
        tirage=(rand(size(v)))<v;
    end
    
    image=reshape(tirage,20,16);
    figure()
    imshow(image)

end

