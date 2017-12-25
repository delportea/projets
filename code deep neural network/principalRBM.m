clear all;
close all;
clc;

num =[11,13];
nb_neur1 = 320; nb_neur2 = 100;
taille=[nb_neur1,nb_neur2];
nb_iter_grad=1000;
learning_rate=0.1;
batch_size=100;
nb_images=10;
nb_iter_Gibbs=3000;

load('binaryalphadigs.mat');


mat = lire_alpha_digit( num,dat );

RBM = init_RBM(nb_neur1,nb_neur2);
RBM = train_RBM( RBM, nb_iter_grad, learning_rate, batch_size, mat)
generer_image_RBM(RBM,nb_iter_Gibbs,nb_images)