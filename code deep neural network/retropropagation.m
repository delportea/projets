function [dbn, cross_entropy] = retropropagation(dbn, donnees, labels, iterations,StepRatio,BatchSize)


strbm = 1;
nrbm = numel( dbn );
num = size(donnees,1);
deltaDbn = dbn;
for n=strbm:nrbm
    deltaDbn{n}.w= zeros(size(dbn{n}.w));
    deltaDbn{n}.b = zeros(size(dbn{n}.b));
end


% We repeat epochs until this criterion is not met
for iter=1:iterations

    
    %We run one epoch
    ind = randperm(num);
    for batch=1:BatchSize:num
        bind = ind(batch:min([batch + BatchSize - 1, num]));
        trainDBN = dbn;
        Hall = entree_sortie_reseau( trainDBN, donnees(bind,:) );
        
        
        for n=nrbm:-1:strbm
            derSgm = Hall{n} .* ( 1 - Hall{n} );
            if( n+1 > nrbm )
                derSgm = Hall{n} .* ( 1 - Hall{n} );
                der = ( Hall{nrbm} - labels(bind,:) );
            else
                der = derSgm .* ( der * trainDBN{n+1}.w' );
            end
            
            if( n-1 > 0 )
                in = Hall{n-1};
            else
                in = donnees(bind,:);
            end
            
            in = cat(2, ones(numel(bind),1), in);
            
            deltaWb = in' * der / numel(bind);
            deltab = deltaWb(1,:);
            deltaW = deltaWb(2:end,:);
              
    
           
            dbn{n}.w = dbn{n}.w - StepRatio * deltaW;
            dbn{n}.b = dbn{n}.b - StepRatio * deltab;
            
        end
        
    end
    
    
    % Rajouter quelques lignes pour calculer l'entropie crois�e
    
        out = entree_sortie_reseau( dbn, donnees );
        out=out{nrbm}; %sortie du r�seau
        
%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%
%%%%%%%%%%cross_entropy= 
      
        cross_entropy = -sum(sum(labels.*log(out))) / numel(out) ;
        fprintf( '%3d : %9.4f\n', iter, cross_entropy );
        
end
