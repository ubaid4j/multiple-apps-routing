import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "./user";

@Component({
    selector: 'app-userinfo',
    standalone: true,
    imports: [],
    templateUrl: './userinfo.component.html',
    styleUrl: './userinfo.component.css'
})
export class UserinfoComponent implements OnInit {
    username: string = '';

    constructor(private httpClient: HttpClient) {
    }

    ngOnInit(): void {
        this.httpClient.get<User>("/api/users/current").subscribe((user: User) => {
            this.username = user.name;
        })
    }
}
