package iexls.services

/**
 * Created by icarokume on 27/09/16.
 */
interface ServiceFactory {

    Map serviceMap()

    def createService(Class serviceClass)

}
