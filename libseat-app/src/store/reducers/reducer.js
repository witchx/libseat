import { combineReducers } from 'redux'
import { UserReducer } from './UserReducer'
import { SeatReducer} from "./SeatReducer";
import {OrderReducer} from "./OrderReducer";
import {CardReducer} from "./CardReducer";

const RootReducer = combineReducers({
    userModule: UserReducer,
    seatModule: SeatReducer,
    orderModule: OrderReducer,
    cardModule: CardReducer
})
export default RootReducer
