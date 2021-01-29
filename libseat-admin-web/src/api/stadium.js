import axios from '@/libs/api.request'

const domain = '/api/stadium';

export function getStadium (params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateStadium (id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'put',
    data: data
  })
}
export function createStadium (data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteStadium (id) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'delete'
  })
}
