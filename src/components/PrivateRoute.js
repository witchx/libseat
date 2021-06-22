import React from 'react'
import { Route, Redirect, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import Cookies from 'js-cookie'
function PrivateRoute({ component: Component, loginState, ...rest }) {
    const token = Cookies.get("token")
    return (

        <Route
            {...rest}
            // 如果已登录，则直接跳转到对应页面，否则重定向到登录页面
            render={props => loginState&&token? <Component {...props}/> :
            // 将前一页面路径信息存入到状态中
                <Redirect to={{ pathname: '/login', state: { from: props.location } }}/>
            }
        >
        </Route>
    )
}
// 映射函数
const mapStateToProps = state => {
    return {
        loginState: state.userModule.loginState
    }
}

export default connect(mapStateToProps)(withRouter(PrivateRoute))
