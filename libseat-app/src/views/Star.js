import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core'
import { faStar } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import '../style/star.scss'
library.add(faStar,far)

export default class Star extends React.Component {
  onHover = e => {
    const { onHover, index } = this.props;
    onHover(e, index);
  };

  onClick = e => {
    const { onClick, index } = this.props;
    onClick(e, index);
  };

  getClassName() {
    // 根据当前评分修改 star 的 class
    const { index, value } = this.props;
    const starValue = index + 1;
    let className = starValue <= value ? 'full' : 'zero';
    return className;
  }

  render() {
    const { onHover, onClick } = this;
    const { index, count, value } = this.props;
    return (
        <li className={this.getClassName()}>
          <div
              onClick={onClick}
              onMouseMove={onHover}
              role="radio"
              aria-checked={value > index ? 'true' : 'false'}
              aria-posinset={index + 1}
              aria-setsize={count}
          >
            {this.getClassName() === 'zero'? <FontAwesomeIcon icon={["far","star"]}/>:<FontAwesomeIcon icon="star"/>}
          </div>
        </li>
    );
  }
}
