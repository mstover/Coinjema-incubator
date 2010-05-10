import strategiclibrary.service.cache.*
import strategiclibrary.nontest.service.sql.mock.*
import org.coinjema.util.*
import strategiclibrary.util.*

System.out.println("Setting up cache")

[new CacheRegistration(cacheTime : TimeConstants.DAY,
           objectType : Product.class,
           mainRetrieval : new Functor(new Product(),"findProducts",[["":""]] as Object[]),
           categoryFunctors : [ "Primary":new Functor("getPrimary"),
                                "id":new Functor("getId"),
                                "Image Type":new Functor("getValue","Image Type") ],
           organizingCategory : "family",
           organizingFunctor : new Functor("getValue","family"),
           primaryPath : ["family",null,"id",null] as Object[],
           primaryFunctors : [new Functor("getValue","family"),new Functor("getId")],
           adHocRetrieval : new Functor(new Product(),"findProducts"))]