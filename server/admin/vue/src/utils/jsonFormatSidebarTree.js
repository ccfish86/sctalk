

export default(routers_ex,routers_data) => {
      //let routers =JSON.parse(routers_data)
      
      let jsonData=[]
      routers_data.forEach(item =>{
              let router_info={
                  id:item.power_id,
                  pid:item.parent_id,
                  name:item.power_name,
                  path:item.power_url,
                  com:item.power_url,
                  hidden: false,
                  icon:"star",
                  leaf:true,
              }
              
              jsonData.push(router_info)
      });

     transData(routers_ex,jsonData,'id','pid','children')

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
                hashVP.leaf = false 
            }

            delete aVal.id
            delete aVal.pid

            hashVP[children].push(aVal);

        }else{
            delete aVal.id
            delete aVal.pid   
            r.push(aVal);   
        }    
    }    
    return r;    
}  