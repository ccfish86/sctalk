
const _import = require('@/router/_import_' + process.env.NODE_ENV)

export default (routerdata) => {
  generaMenu(routerdata)

}

function generaMenu(routerdata){

  routerdata.forEach( item =>{
      if (typeof item !== 'undefined') { 
          item.component=_import(item.com)
          delete item.com
          
          if (item.children && item.children instanceof Array) { 
                generaMenu(item.children);
          }
      }
     
  })
}