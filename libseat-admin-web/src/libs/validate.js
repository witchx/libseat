export const validateUsername = (rule, value, callback) => {
  if (!isvalidUsername(value)) {
    callback(new Error('请输入正确的用户名'))
  } else {
    callback()
  }
};
export const validatePass = (rule, value, callback) => {
  if (validateStr(value)) {
    callback(new Error('请输入密码'))
  } else if (!validatePassword(value) ) {
    callback(new Error('请输入6-20位英文字母、数字或者符号（除空格），且字母、数字和标点符号至少包含两种'))
  }else {
    callback()
  }
};
/* 合法手机号*/
export function validatePhone(rule, value,callback) {
  const reg =/^[1][3-9][0-9]{9}$/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}
/* 合法邮箱*/
export function validateEMail(rule, value,callback) {
  const reg =/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
  if(value==''||value==undefined||value==null){
    callback();
  }else{
    if (!reg.test(value)){
      callback(new Error('请输入正确的邮箱'));
    } else {
      callback();
    }
  }
}
/* 合法用户名*/
export function isvalidUsername(str) {
  return str.trim().length>=3
}
/* 合法密码*/
export function validatePassword(str) {
  const reg = /^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$)([^\u4e00-\u9fa5\s]){6,20}$/
  return reg.test(str)
}
/* 合法字符串*/
export function validateStr(str) {
  return !str || str === ''
}
/* 合法uri*/
export function validateURL(textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}

/* 小写字母*/
export function validateLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/* 大写字母*/
export function validateUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/* 大小写字母*/
export function validatAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

