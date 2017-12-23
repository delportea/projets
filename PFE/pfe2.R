library(Matrix)
require(Matrix)

N <- 500
mu1 <- 0.5
theta1 <- c(1,0.6,0.4)
mu2 <- 0.3
theta2 <- c(1,0.3,0.8)
mu3 <- 0.8
theta3 <- c(0.5,0.5,0.5)
Y0 <- 0
epsilon <- rnorm(N+1,mean=0,sd=1)

ynplusun <- function(Y0,n,mu,theta) {
  if (n < 2) return (Y0+mu+theta[1]*epsilon[2]+theta[2]*epsilon[1]+mu)
  else return (theta[1]*epsilon[n+1]+theta[2]*epsilon[n]+theta[3]*epsilon[n-1]+mu+ynplusun(Y0,n-1,mu,theta))
}

ysimul <- function(Y0,a,b,mu,theta) {
  Y <- Y0
  for (i in a:b) {
    Y <- c(Y,ynplusun(Y0,i,mu,theta))
  }
  return(Y)
}

Y1 <- ysimul(Y0,1,N,mu1,theta1)
Y2 <- ysimul(Y0,1,N,mu2,theta2)
Y3 <- ysimul(Y0,1,N,mu3,theta3)

plot(Y1[1:N+1])
plot(Y2[1:N+1])
plot(Y3[1:N+1])


predict_kalman <- function(Y0,N,t,theta,mu) {
  A <- rbind(c(1,theta[2],theta[3]),c(0,0,0),c(0,1,0))
  B <- cbind(c(mu,0,0))
  Q <- rbind(c(theta[1]*theta[1],theta[1],0),c(theta[1],1,0),c(0,0,0))
  
  x_n_n <- list()
  gamma_n_n <- list()
  x_nplusun_n <- list()
  gamma_nplusun_n <- list()
  x_n_n[[1]] <- cbind(c(Y0,0,0))
  gamma_n_n[[1]] <- rbind(c(0,0,0),c(0,1,0),c(0,0,1))
  epsilon_estime <- 0
  Y <- c(Y0)
  
  for (i in 1:(N+t)) {
    x_nplusun_n[[i]] <- A%*%x_n_n[[i]]+B
    gamma_nplusun_n[[i]] <- A%*%gamma_n_n[[i]]%*%t(A)+Q
    delta <- cbind(c(gamma_nplusun_n[[i]][1],gamma_nplusun_n[[i]][4],gamma_nplusun_n[[i]][7]))
    eta <- gamma_nplusun_n[[i]][1]
    epsilon_estime <- c(epsilon_estime,x_nplusun_n[[i]][1])
    if (i > 1) {
      Y <- c(Y,Y[i]+theta[1]*epsilon_estime[i+1]+theta[2]*epsilon_estime[i]+theta[3]*epsilon_estime[i-1]+mu)
    }
    else {
      Y <- c(Y,Y[i]+theta[1]*epsilon_estime[i+1]+theta[2]*epsilon_estime[i]+mu)
    }
    x_n_n[[i+1]] <- x_nplusun_n[[i]]+(Y[i+1]-x_nplusun_n[[i]][1])/eta*delta
    gamma_n_n[[i+1]] <- gamma_nplusun_n[[i]]-(delta%*%t(delta))/eta
  }
  return (Y)
}


predict_kalman2 <- function(Y,N,t,theta,mu,epsilon) {
  A <- rbind(c(1,theta[2],theta[3]),c(0,0,0),c(0,1,0))
  B <- cbind(c(mu,0,0))
  Q <- rbind(c(theta[1]*theta[1],theta[1],0),c(theta[1],1,0),c(0,0,0))
  
  x_n_n <- list()
  gamma_n_n <- list()
  x_nplusun_n <- list()
  gamma_nplusun_n <- list()
  x_n_n[[1]] <- cbind(c(Y[1],0,0))
  gamma_n_n[[1]] <- rbind(c(0,0,0),c(0,1,0),c(0,0,1))
  epsilon_estime <- c(epsilon[1])
  Y_estime <- c(Y[1])
  
  for (i in 1:(N+t)) {
    x_nplusun_n[[i]] <- A%*%x_n_n[[i]]+B
    gamma_nplusun_n[[i]] <- A%*%gamma_n_n[[i]]%*%t(A)+Q
    delta <- cbind(c(gamma_nplusun_n[[i]][1],gamma_nplusun_n[[i]][4],gamma_nplusun_n[[i]][7]))
    eta <- gamma_nplusun_n[[i]][1]
    
    if (i > N+1) {
      epsilon_estime <- c(epsilon_estime,rnorm(1,mean=0,sd=(gamma_nplusun_n[[i]][2])^2))
      Y_estime <- c(Y_estime,Y_estime[i]+theta[1]*epsilon_estime[i+1]+theta[2]*epsilon_estime[i]+theta[3]*epsilon_estime[i-1]+mu)
    }
    else {
      epsilon_estime <- c(epsilon_estime,epsilon[i+1])
      Y_estime <- c(Y_estime,Y[i+1])
    }
    x_n_n[[i+1]] <- x_nplusun_n[[i]]+(Y_estime[i+1]-x_nplusun_n[[i]][1])/eta*delta
    gamma_n_n[[i+1]] <- gamma_nplusun_n[[i]]-(delta%*%t(delta))/eta
  }
  return (Y_estime)
}

predict_kalman3 <- function(Y,N,t,theta,mu) {
  A <- rbind(c(1,theta[2],theta[3]),c(0,0,0),c(0,1,0))
  B <- cbind(c(mu,0,0))
  Q <- rbind(c(theta[1]*theta[1],theta[1],0),c(theta[1],1,0),c(0,0,0))
  
  x_n_n <- list()
  gamma_n_n <- list()
  x_nplusun_n <- list()
  gamma_nplusun_n <- list()
  x_n_n[[1]] <- cbind(c(Y[1],0,0))
  gamma_n_n[[1]] <- rbind(c(0,0,0),c(0,1,0),c(0,0,1))
  epsilon_estime <- rep(N+t)
  Y_estime <- c(Y[1])
  
  for (i in 1:(N+t)) {
    if (i < N) {
      x_nplusun_n[[i]] <- A%*%x_n_n[[i]]+B
      gamma_nplusun_n[[i]] <- A%*%gamma_n_n[[i]]%*%t(A)+Q
      delta <- cbind(c(gamma_nplusun_n[[i]][1],gamma_nplusun_n[[i]][4],gamma_nplusun_n[[i]][7]))
      eta <- gamma_nplusun_n[[i]][1]
      x_n_n[[i+1]] <- x_nplusun_n[[i]]+(Y[i+1]-x_nplusun_n[[i]][1])/eta*delta
      gamma_n_n[[i+1]] <- gamma_nplusun_n[[i]]-(delta%*%t(delta))/eta
      ## vraisemblance seq = log(pnorm(Y[i+1], x_nplusun_n[[i]][1], sqrt(eta))
      
      print(x_nplusun_n[[i]])
      print(x_n_n[[i+1]])
      epsilon_estime[i+1] <- x_n_n[[i+1]][2]
      epsilon_estime[i] <- x_n_n[[i+1]][3]
      X_estime <- x_n_n[[i+1]]
      ## break()
    }
    
    

    if (i > N+1) {
      X_estime = A%*%X_estime+B
      Y_estime <- c(Y_estime,X_estime[1])
      
    }
    else {
      Y_estime <- c(Y_estime,Y[i+1])
    }
    
    
  }
  return (Y_estime)
}

prediction = function(K,Y,N,t,theta,mu,epsilon) {
  Y_prediction <-predict_kalman2(Y,N,t,theta,mu,epsilon)
  for (i in 2:K) {
    Y_prediction <- Y_prediction + predict_kalman2(Y,N,t,theta,mu,epsilon)
  }
  return (Y_prediction/K)
}

meilleur_prediction = function(K,Y,N,t,theta,mu) {
  Y_prediction <-predict_kalman3(Y,N,t,theta,mu)
  erreur <- mean((Y_prediction[501:521]-Y[501:521])^2)
  for (i in 2:K) {
    Y_prediction2 <- predict_kalman3(Y,N,t,theta,mu)
    erreur2 <- mean((Y_prediction[501:521]-Y[501:521])^2)
    if (erreur2 < erreur) {
      Y_prediction <- Y_prediction2
      erreur <- erreur2
    }
  }
  return (Y_prediction)
}

erreur <- mean((Y2[501:521]-Y1[501:521])^2)
erreur
epsilon <- rnorm(N+1,mean=0,sd=1)
Y1 <- ysimul(Y0,1,N,mu1,theta1)
Y2 = predict_kalman3(Y1,500,20,theta1,mu1)

x <- c(1:501)
y1 <- Y1
y2 <- c(Y1[1:500],Y2[501:521])
x <- c(1:521)
plot(x, y2, type = "n", ylim = range(c(y1, y2)), xlab = "", ylab = "")
plot(x, y2, type = "n", ylim = range(c(1, 521)), xlab = "", ylab = "")
x <- c(1:501)
lines(x, y1, col = "blue")
x <- c(1:521)
lines(x, y2, col = "red")


x <- c(1:501)
lines(x, Y1, col = "blue")
x <- c(1:521)
lines(x, Y2, col = "purple")


grille <- function(Y,N,t,theta,mu) {
  A <- rbind(c(1,theta[2],theta[3]),c(0,0,0),c(0,1,0))
  B <- cbind(c(mu,0,0))
  Q <- rbind(c(theta[1]*theta[1],theta[1],0),c(theta[1],1,0),c(0,0,0))
  phi<-list()
  x_n_n <- list()
  gamma_n_n <- list()
  x_nplusun_n <- list()
  gamma_nplusun_n <- list()
  x_n_n[[1]] <- cbind(c(Y[1],0,0))
  gamma_n_n[[1]] <- rbind(c(0,0,0),c(0,1,0),c(0,0,1))
  epsilon_estime <- rep(N+t)
  Y_estime <- c(Y[1])
  
  for (i in 1:(N+t)) {
    if (i < N) {
      x_nplusun_n[[i]] <- A%*%x_n_n[[i]]+B
      gamma_nplusun_n[[i]] <- A%*%gamma_n_n[[i]]%*%t(A)+Q
      delta <- cbind(c(gamma_nplusun_n[[i]][1],gamma_nplusun_n[[i]][4],gamma_nplusun_n[[i]][7]))
      eta <- gamma_nplusun_n[[i]][1]
      x_n_n[[i+1]] <- x_nplusun_n[[i]]+(Y[i+1]-x_nplusun_n[[i]][1])/eta*delta
      gamma_n_n[[i+1]] <- gamma_nplusun_n[[i]]-(delta%*%t(delta))/eta
      ## vraisemblance seq = log(pnorm(Y[i+1], x_nplusun_n[[i]][1], sqrt(eta))
      
      phi[[i]]<-dnorm(x,mean=x_nplusun_n[[i]][1],sd=eta)
      for (mu in {0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8;0.9;1.0} ){
        for ( theta1 in {0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8;0.9;1.0} ){
          for ( theta2 in {0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8;0.9;1.0} ){
            for ( theta3 in {0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8;0.9;1.0} ){
              if (isTRUE(phi[[i]]==max(phi[[i]]))){
                return(w<-c(mu,theta1,theta2,theta3))
              }
              
              
            }
          }
        }
      }
      
      #print(phi[[i]])
      #print(x_n_n[[i+1]])
      #epsilon_estime[i+1] <- x_n_n[[i+1]][2]
      #epsilon_estime[i] <- x_n_n[[i+1]][3]
      #X_estime <- x_n_n[[i+1]]
    }
  }
  Y <- c(Y,Y[i]+theta[1]*epsilon_estime[i+1]+theta[2]*epsilon_estime[i]+theta[3]*epsilon_estime[i-1]+mu)
}

grille(Y,N,20, theta1,mu1)
x
