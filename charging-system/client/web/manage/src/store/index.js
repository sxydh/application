import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const state = {
    user: null,
    wallet: null,
    auth: { flush: true },
    authorization: null,
    walletPageData: null,
    walletPageData: null
};
const getters = {
    takeWaterPageData: (state) => {
        let result = state.waterPageData == null ? localStorage.getItem('waterPageData') : state.waterPageData;
        console.log('store -> getters -> takeWaterPageData -> ' + result);
        return JSON.parse(result);
    },
    takeWalletPageData: (state) => {
        let result = state.walletPageData == null ? localStorage.getItem('walletPageData') : state.walletPageData;
        console.log('store -> getters -> takeWalletPageData -> ' + result);
        return JSON.parse(result);
    },
    takeWallet: (state) => {
        let result = state.wallet == null ? localStorage.getItem('wallet') : state.wallet;
        console.log('store -> getters -> takeWallet -> ' + result);
        return JSON.parse(result);
    },
    takeWater: (state) => {
        let result = state.water == null ? localStorage.getItem('water') : state.water;
        console.log('store -> getters -> takeWater -> ' + result);
        return JSON.parse(result);
    },
    takeUser: (state) => {
        let result = state.user == null ? localStorage.getItem('user') : state.user;
        console.log('store -> getters -> takeUser -> ' + result);
        return JSON.parse(result);
    },
    takeAuth: (state) => {
        let result = state.auth;
        console.log('store -> getters -> takeAuth -> ' + JSON.stringify(result));
        return result;
    },
    takeAuthorization: (state) => {
        let result = state.authorization == null ? localStorage.getItem('authorization') : state.authorization;
        console.log('store -> getters -> takeAuthorization -> ' + result);
        return JSON.parse(result);
    },
};
const mutations = {
    updateWaterPageData(state, value) {
        value = JSON.stringify(value);
        console.log('store -> mutations -> updateWaterPageData -> ' + value);
        state.waterPageData = value;
        window.localStorage.setItem('waterPageData', value);
    },
    updateWalletPageData(state, value) {
        value = JSON.stringify(value);
        console.log('store -> mutations -> updateWalletPageData -> ' + value);
        state.walletPageData = value;
        window.localStorage.setItem('walletPageData', value);
    },
    updateWallet(state, value) {
        value = JSON.stringify(value);
        console.log('store -> mutations -> updateWallet -> ' + value);
        state.wallet = value;
        window.localStorage.setItem('wallet', value);
    },
    updateWater(state, value) {
        value = JSON.stringify(value);
        console.log('store -> mutations -> updateWater -> ' + value);
        state.water = value;
        window.localStorage.setItem('water', value);
    },
    updateUser(state, value) {
        value = JSON.stringify(value);
        console.log('store -> mutations -> updateUser -> ' + value);
        state.user = value;
        window.localStorage.setItem('user', value);
    },
    updateAuth(state, value) {
        console.log('store -> mutations -> updateAuth -> ' + value);
        state.auth = value;
    },
    updateAuthorization(state, value) {
        value = JSON.stringify(value);
        console.log('store -> mutations -> updateAuthorization -> ' + value);
        state.authorization = value;
        window.localStorage.setItem('authorization', value);
    }
};
const actions = {

};

export default new Vuex.Store({
    state,
    getters,
    mutations,
    actions
})
