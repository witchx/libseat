import axios from '@/libs/api.request'

const domain = '/api/upload';

export function uploadImg (formData){
  return axios.request({
    url: domain+'/image',
    method: 'post',
    data: formData
  })
}
