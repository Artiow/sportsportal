import qs from 'qs';

export default class ParamsSerializer {

    static stringify() {
        return (params => qs.stringify(params, {arrayFormat: 'repeat'}));
    }
}