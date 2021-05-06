const supply0 = num => num < 10 ? `0${num}` : `${num}`;

export const timestampToTime = (timestamp) => {
    const date = new Date(parseInt(timestamp));
    const Y = `${date.getFullYear()}`;
    const M = `${supply0(date.getMonth() + 1)}`;
    const D = supply0(date.getDate());
    const h = supply0(date.getHours());
    const m = supply0(date.getMinutes());
    return `${Y}-${M}-${D} ${h}:${m}`;
};

export const datToTime = (date) => {

    const Y = `${date.getFullYear()}`;
    const M = `${supply0(date.getMonth() + 1)}`;
    const D = supply0(date.getDate());
    const h = supply0(date.getHours());
    const m = supply0(date.getMinutes());
    return `${Y}-${M}-${D} ${h}:${m}`;
};

export const strToTime = (str) => {
    const date = new Date(str);
    const Y = `${date.getFullYear()}`;
    const M = `${supply0(date.getMonth() + 1)}`;
    const D = supply0(date.getDate());
    const h = supply0(date.getHours());
    const m = supply0(date.getMinutes());
    return `${Y}-${M}-${D} ${h}:${m}`;
};

export const timestampTohm = (timestamp) => {
    const date = new Date(timestamp);
    const h = supply0(date.getHours());
    const m = supply0(date.getMinutes());
    return `${h}:${m}`;
};
