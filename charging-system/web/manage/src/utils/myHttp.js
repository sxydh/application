import axios from 'axios'
import { Message } from 'element-ui';
import router from '../router/index'
import store from '../store/index'

export default {
    asyncPost(config) {
        let url = process.env.API_ROOT + config.url;
        let data = config.data;

        let authorization = store.getters.takeAuthorization;
        console.log('myHttp -> asyncPost( ' + JSON.stringify(config) + ' )');
        return new Promise((resolve) => {
            axios({
                withCredentials: true,
                method: 'POST',
                headers: { 'authorization': authorization, 'Content-Type': 'application/json' },
                url: url,
                data: data
            }).then((suc) => {
                console.log('return -> ' + JSON.stringify(suc.data) + ' from myHttp -> asyncPost( ' + JSON.stringify(config) + ' )');
                if (suc.data.msg) {
                    if (suc.data.sc != 401) {
                        Message.error(suc.data.msg);
                    }
                    resolve([null, suc.data.msg]);
                    if (suc.data.sc == 401) {
                        router.push({ path: "/entrance/login" });
                    }
                    return;
                }
                resolve([suc.data, null]);
            }).catch(error => {
                console.log('catch -> ' + error + ' from myHttp -> asyncPost( ' + JSON.stringify(config) + ' )');
                Message.error(error + '');
                resolve([null, error]);
            })
        });
    },
    asyncGet(config) {
        let url = process.env.API_ROOT + config.url;

        let authorization = store.getters.takeAuthorization;
        console.log('myHttp -> asyncGet( ' + JSON.stringify(config) + ' )');
        return new Promise((resolve) => {
            axios({
                withCredentials: true,
                method: 'GET',
                headers: { 'authorization': authorization, 'Content-Type': 'application/json' },
                url: url,
            }).then((suc) => {
                console.log('return -> ' + JSON.stringify(suc.data) + ' from myHttp -> asyncGet( ' + JSON.stringify(config) + ' )');
                if (suc.data.msg) {
                    if (suc.data.sc != 401) {
                        Message.error(suc.data.msg);
                    }
                    resolve([null, suc.data.msg]);
                    if (suc.data.sc == 401) {
                        router.push({ path: "/entrance/login" });
                    }
                    return;
                }
                resolve([suc.data, null]);
            }).catch(error => {
                console.log('catch -> ' + error + ' from myHttp -> asyncGet( ' + JSON.stringify(config) + ' )');
                Message.error(error + '');
                resolve([null, error]);
            })
        });
    }
}