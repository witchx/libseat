import axios from '@/libs/api.request'

const domain  = '/api/room';

export function getRoom(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function getRoomByStadiumId(params) {
  return axios.request({
    url:domain+'/get',
    method:'get',
    params:params
  })
}
export function updateRoom (id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'put',
    data: data
  })
}
export function createRoom(data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteRoom (id) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'delete'
  })
}
