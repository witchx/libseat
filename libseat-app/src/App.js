import React from 'react'
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import Layout from './layout/Layout'
import Home from './views/Home'
import Card from './views/Card'
import OrderSeat from './views/OrderSeat'
import My from './views/My'
import MyNoLogin from './views/MyNoLogin'
import Pay from './views/Pay'
import OrderList from './views/OrderList'
import Login from './views/Login'
import Register from './views/Register'
import ErrorPage from './views/ErrorPage'
import PrivateRoute from './components/PrivateRoute'
import MySetting from "./views/MySetting";
import RoomList from "./views/RoomList";
import RoomDetail from "./views/RoomDetail";
import OrderDetail from "./views/OrderDetail";
import Result from "./views/Result";
import MyCard from "./views/MyCard";
import MyRank from "./views/MyRank";

function App() {
  return (
    <Router basename={process.env.PUBLIC_URL}>
      <div style={{ height: '100%' }}>
        <Switch>
          {/*首页*/}
          <Route path='/' exact render={props => <Layout {...props}><Home></Home></Layout>}></Route>
          <Route path='/roomList' render={props =>  <RoomList {...props}></RoomList>}></Route>
          <Route path='/roomDetail' render={props =>  <RoomDetail {...props}></RoomDetail>}></Route>
          {/*加入会员*/}
          <Route path='/card' render={props => <Layout {...props}><Card></Card></Layout>}></Route>
          {/*个人中心*/}
          <Route path='/login' render={props => <Layout {...props}><Login></Login></Layout>}></Route>
          <Route path='/register' render={props => <Layout {...props}><Register></Register></Layout>}></Route>
          <Route path='/mynologin' render={props => <Layout {...props}><MyNoLogin></MyNoLogin></Layout>}></Route>
          <Route path='/my' render={props => <Layout {...props}><PrivateRoute component={My}></PrivateRoute></Layout>}></Route>
          <Route path='/myCard' render={props => <PrivateRoute {...props} component={MyCard}></PrivateRoute>}></Route>
          <Route path='/rank' render={props => <PrivateRoute {...props} component={MyRank}></PrivateRoute>}></Route>
          <Route path='/setting' render={props => <PrivateRoute {...props} component={MySetting}></PrivateRoute>}></Route>
          {/*使用记录*/}
          <Route path='/order' render={props => <PrivateRoute {...props} component={OrderList}></PrivateRoute>}></Route>
          <Route path='/orderDetail/:data' render={props => <PrivateRoute {...props} component={OrderDetail}></PrivateRoute>}></Route>
          <Route path='/seatOrder' render={props => <Layout {...props}><PrivateRoute path="/seatOrder" component={OrderSeat}></PrivateRoute></Layout>}></Route>
          {/*支付*/}
          <Route path='/pay' render={props => <PrivateRoute {...props} component={Pay}></PrivateRoute>}></Route>
          <Route path='/result/:id' render={props => <PrivateRoute {...props} component={Result}></PrivateRoute>}></Route>
          {/*404*/}
          <Route path='/404' render={props => <ErrorPage {...props}></ErrorPage>}></Route>
          <Redirect to="/404"></Redirect>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
