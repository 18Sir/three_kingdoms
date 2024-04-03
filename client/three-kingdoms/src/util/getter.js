import stores from "@/stores";

export const getUserData = () => {
    if(Object.keys(stores.getters.getUserData).length < 1){
        stores.dispatch('setUserData')
    }
    return stores.getters.getUserData;
}