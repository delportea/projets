clear all
close all
clc

mnistfile{1}='train-images-idx3-ubyte';
mnistfile{2}='train-labels-idx1-ubyte';
mnistfile{3}='t10k-images-idx3-ubyte';
mnistfile{4}='t10k-labels-idx1-ubyte';

[images_train, labels_train, images_test, labels_test] = mnistread(mnistfile);

%Paramètres

learning_rate = 0.1;
batch_size = 100;
nb_iter_grad = 100;
nb_iter_Gibbs = 200;
nb_classes=10;
StepRatio_retro=0.1;
BatchSize_retro=100;

taille_apprentissage = 5000;

random_vect = randperm(taille_apprentissage);
images_train = images_train(random_vect,:);
labels_train = labels_train(random_vect,:);

ind=randperm(size(images_train,1));
images_train=images_train(ind(1:taille_apprentissage),:);
images_train=images_train(ind(1:taille_apprentissage),:);


taille_reseau = [size(images_train,2),2000,2000,nb_classes];

%Initialisation des DNN

DNN_classique = init_DNN(taille_reseau);

DNN_preentraine = init_DNN(taille_reseau);
DNN_preentraine = train_DBN(DNN_preentraine,nb_iter_grad,learning_rate,batch_size,images_train);

[DNN_classique, cross_entropy_classique] = retropropagation(DNN_classique, images_train, labels_train, nb_iter_grad,StepRatio_retro,BatchSize_retro);
[DNN_preentraine, cross_entropy_preentraine] = retropropagation(DNN_preentraine, images_train, labels_train, nb_iter_grad,StepRatio_retro,BatchSize_retro);


erreur_classique =test_DNN(DNN_classique, images_test, labels_test)
erreur_preentraine = test_DNN(DNN_preentraine, images_test, labels_test)


