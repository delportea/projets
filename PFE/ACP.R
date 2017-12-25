
library(MASS);
data(crabs);
crabsquant<-crabs[,4:8];
princomp(crabs);
biplot(x, choices = 1:2, scale = 1, pc.biplot = FALSE, ...);

res<- princomp(crabsquant)
summary(res)
crabsquant/crabsquant[,3][,-3]
plot(res$scores[,1:2],col=c("blue","orange")[crabs$sp],pch=c(20,21)[crabs$sex])

rho1<-res$loadings[,1]*res$sdev[1]
rho2<-res$loadings[,2]*res$sdev[2]

c1 <- acp.autos$loadings[,1]*acp.autos$sdev[1]
c2 <- acp.autos$loadings[,2]*acp.autos$sdev[2]

#affichage
correlation <- cbind(c1,c2)
print(correlation,digits=2)

#carrés de la corrélation (cosinus²)
print(correlation^2,digits=2)

#cumul carrés de la corrélation
print(t(apply(correlation^2,1,cumsum)),digits=2)

#*** cercle des corrélations - variables actives ***
plot(rho1,rho2,xlim=c(-2,+2),ylim=c(-2,+2),type="n")
abline(h=0,v=0)
text(rho1,rho2,labels=colnames(crabsquant),cex=1.5)
symbols(0,0,circles=1,inches=F,add=T)







#exercice 2.2
data


load("~/Downloads/DonneesSNPnormalisees.RData")
prcomp(data$scaled.Geno)
