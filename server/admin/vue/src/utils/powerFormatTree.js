

export default(tree_ex,tree_data) => {
      
      let jsonData=[]
      tree_data.forEach(item =>{
              let router_info={
                  id:item.power_id,
                  pid:item.parent_id,
                  label:item.power_name,
              }
              
              jsonData.push(router_info)
      });

     transData(tree_ex,jsonData,'id','pid','children')

}


function transData(r,a, idStr, pidStr, chindrenStr){    
    var hash = {}, id = idStr, pid = pidStr;
    var children = chindrenStr, i = 0, j = 0, len = a.length;
    

    for(; i < len; i++){    
        hash[a[i][id]] = a[i];    
    }    
    for(; j < len; j++){    
        var aVal = a[j], hashVP = hash[aVal[pid]];
        
        if(hashVP){    

            if(!hashVP[children]){
                hashVP[children] = []
            }

            delete aVal.pid

            hashVP[children].push(aVal);

        }else{
            delete aVal.pid   
            r.push(aVal);   
        }    
    }    
    return r;    
}  