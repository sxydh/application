import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const state = {
    user: null,
    water: null,
    wallet: null,
    auth: { flush: true }
};
const getters = {
    takeUser: (state) => {
        let result = state.user == null ? localStorage.getItem('user') : state.user;
        console.log('store -> getters -> takeUser -> ' + result);
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
    takeAuth: (state) => {
        let result = state.auth;
        console.log('store -> getters -> takeAuth -> ' + JSON.stringify(result));
        return result;
    }
};
const mutations = {
    updateUser(state, value) {
        value = JSON.stringify(value);
        console.log('store -> mutations -> updateUser -> ' + value);
        state.user = value;
        window.localStorage.setItem('user', value);
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
    updateAuth(state, value) {
        console.log('store -> mutations -> updateAuth -> ' + value);
        state.auth = value;
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
